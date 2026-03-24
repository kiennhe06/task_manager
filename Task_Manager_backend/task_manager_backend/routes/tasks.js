const express = require("express");
const { body, validationResult } = require("express-validator");
const Task = require("../models/Task");
const requireAuth = require("../middleware/auth");

const router = express.Router();

router.use(requireAuth);

router.get("/", async (req, res, next) => {
  try {
    const filter = { userId: req.user.id };
    if (req.query.status) {
      filter.status = req.query.status;
    }
    const tasks = await Task.find(filter).sort({ createdAt: -1 });
    res.json(tasks);
  } catch (error) {
    next(error);
  }
});

router.post(
  "/",
  [body("title").trim().notEmpty().withMessage("Vui long nhap tieu de cong viec")],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() });
      }

      const task = await Task.create({
        userId: req.user.id,
        title: req.body.title,
        description: req.body.description || "",
        priority: req.body.priority || "medium",
        status: req.body.status || "todo",
        dueDate: req.body.dueDate || null
      });

      res.status(201).json(task);
    } catch (error) {
      next(error);
    }
  }
);

router.put("/:id", async (req, res, next) => {
  try {
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
      return res.status(404).json({ message: "Khong tim thay cong viec" });
    }
    return res.json(updatedTask);
  } catch (error) {
    next(error);
  }
});

router.delete("/:id", async (req, res, next) => {
  try {
    const deletedTask = await Task.findOneAndDelete({
      _id: req.params.id,
      userId: req.user.id
    });
    if (!deletedTask) {
      return res.status(404).json({ message: "Khong tim thay cong viec" });
    }
    return res.json({ message: "Da xoa cong viec" });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
