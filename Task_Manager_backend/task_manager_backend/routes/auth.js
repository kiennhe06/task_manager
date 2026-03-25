const express = require("express");
const { body, validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../models/User");
const Task = require("../models/Task");
const requireAuth = require("../middleware/auth");
const { sendSuccess, sendError } = require("../utils/response");

const router = express.Router();

function signToken(userId) {
  return jwt.sign({ userId }, process.env.JWT_SECRET || "dev_secret", {
    expiresIn: "7d"
  });
}

// GET current user profile
router.get("/profile", requireAuth, async (req, res, next) => {
  try {
    const user = await User.findById(req.user.id).select("-password");
    if (!user) {
      return sendError(res, "Không tìm thấy người dùng", 404);
    }
    const taskCount = await Task.countDocuments({ userId: req.user.id });
    return sendSuccess(res, {
      id: user._id,
      fullName: user.fullName,
      email: user.email,
      createdAt: user.createdAt,
      taskCount
    }, "Lấy thông tin người dùng thành công");
  } catch (error) {
    next(error);
  }
});

router.post(
  "/register",
  [
    body("fullName").trim().notEmpty().withMessage("Vui lòng nhập họ tên"),
    body("email").isEmail().withMessage("Email không hợp lệ"),
    body("password")
      .isLength({ min: 6 })
      .withMessage("Mật khẩu phải có ít nhất 6 ký tự")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return sendError(res, errors.array()[0].msg, 400, errors.array());
      }

      const { fullName, email, password } = req.body;
      const existingUser = await User.findOne({ email });
      if (existingUser) {
        return sendError(res, "Email đã được sử dụng", 409);
      }

      const hashedPassword = await bcrypt.hash(password, 10);
      const user = await User.create({
        fullName,
        email,
        password: hashedPassword
      });

      const token = signToken(user._id.toString());
      return sendSuccess(res, {
        token,
        user: {
          id: user._id,
          fullName: user.fullName,
          email: user.email
        }
      }, "Đăng ký thành công", 201);
    } catch (error) {
      next(error);
    }
  }
);

router.post(
  "/login",
  [
    body("email").isEmail().withMessage("Email không hợp lệ"),
    body("password").notEmpty().withMessage("Vui lòng nhập mật khẩu")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return sendError(res, errors.array()[0].msg, 400, errors.array());
      }

      const { email, password } = req.body;
      const user = await User.findOne({ email });
      if (!user) {
        return sendError(res, "Thông tin đăng nhập không đúng", 401);
      }

      const isMatch = await bcrypt.compare(password, user.password);
      if (!isMatch) {
        return sendError(res, "Thông tin đăng nhập không đúng", 401);
      }

      const token = signToken(user._id.toString());
      return sendSuccess(res, {
        token,
        user: {
          id: user._id,
          fullName: user.fullName,
          email: user.email
        }
      }, "Đăng nhập thành công");
    } catch (error) {
      next(error);
    }
  }
);

module.exports = router;
