const express = require("express");
const { body, validationResult } = require("express-validator");
const Task = require("../models/Task");
const requireAuth = require("../middleware/auth");
const { sendSuccess, sendError } = require("../utils/response");

const router = express.Router();

router.use(requireAuth);

// GET task statistics
router.get("/stats", async (req, res, next) => {
  try {
    const userId = req.user.id;
    const now = new Date();
    const [total, todo, inProgress, done, overdue] = await Promise.all([
      Task.countDocuments({ userId }),
      Task.countDocuments({ userId, status: "todo" }),
      Task.countDocuments({ userId, status: "in_progress" }),
      Task.countDocuments({ userId, status: "done" }),
      Task.countDocuments({ userId, status: { $ne: "done" }, dueDate: { $lt: now } })
    ]);
    return sendSuccess(res, { total, todo, inProgress, done, overdue }, "Thống kê công việc");
  } catch (error) {
    next(error);
  }
});

// GET all tasks (with optional search & status filter)
router.get("/", async (req, res, next) => {
  try {
    const filter = { userId: req.user.id };
    if (req.query.status) {
      filter.status = req.query.status;
    }
    if (req.query.search) {
      filter.title = { $regex: req.query.search, $options: "i" };
    }
    const tasks = await Task.find(filter).sort({ createdAt: -1 });
    return sendSuccess(res, tasks, "Lấy danh sách công việc thành công");
  } catch (error) {
    next(error);
  }
});

// CREATE task
router.post(
  "/",
  [
    body("title").trim().notEmpty().withMessage("Vui lòng nhập tiêu đề công việc"),
    body("status")
      .optional()
      .isIn(["todo", "in_progress", "done"])
      .withMessage("Trạng thái phải là todo, in_progress hoặc done"),
    body("priority")
      .optional()
      .isIn(["low", "medium", "high"])
      .withMessage("Ưu tiên phải là low, medium hoặc high"),
    body("dueDate")
      .optional({ values: "falsy" })
      .isISO8601()
      .withMessage("Hạn chót phải là ngày hợp lệ (YYYY-MM-DD)")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return sendError(res, errors.array()[0].msg, 400, errors.array());
      }

      const task = await Task.create({
        userId: req.user.id,
        title: req.body.title,
        description: req.body.description || "",
        priority: req.body.priority || "medium",
        status: req.body.status || "todo",
        dueDate: req.body.dueDate || null
      });

      return sendSuccess(res, task, "Tạo công việc thành công", 201);
    } catch (error) {
      next(error);
    }
  }
);

// UPDATE task
router.put(
  "/:id",
  [
    body("status")
      .optional()
      .isIn(["todo", "in_progress", "done"])
      .withMessage("Trạng thái phải là todo, in_progress hoặc done"),
    body("priority")
      .optional()
      .isIn(["low", "medium", "high"])
      .withMessage("Ưu tiên phải là low, medium hoặc high"),
    body("dueDate")
      .optional({ values: "falsy" })
      .isISO8601()
      .withMessage("Hạn chót phải là ngày hợp lệ (YYYY-MM-DD)")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return sendError(res, errors.array()[0].msg, 400, errors.array());
      }

      const updatedTask = await Task.findOneAndUpdate(
        { _id: req.params.id, userId: req.user.id },
        {
          $set: {
            title: req.body.title,
            description: req.body.description,
            status: req.body.status,
            priority: req.body.priority,
            dueDate: req.body.dueDate
          }
        },
        { new: true, runValidators: true }
      );

      if (!updatedTask) {
        return sendError(res, "Không tìm thấy công việc", 404);
      }
      return sendSuccess(res, updatedTask, "Cập nhật công việc thành công");
    } catch (error) {
      next(error);
    }
  }
);

// DELETE task
router.delete("/:id", async (req, res, next) => {
  try {
    const deletedTask = await Task.findOneAndDelete({
      _id: req.params.id,
      userId: req.user.id
    });
    if (!deletedTask) {
      return sendError(res, "Không tìm thấy công việc", 404);
    }
    return sendSuccess(res, null, "Đã xóa công việc");
  } catch (error) {
    next(error);
  }
});

module.exports = router;
