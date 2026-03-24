const express = require("express");
const { body, validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../models/User");

const router = express.Router();

function signToken(userId) {
  return jwt.sign({ userId }, process.env.JWT_SECRET || "dev_secret", {
    expiresIn: "7d"
  });
}

router.post(
  "/register",
  [
    body("fullName").trim().notEmpty().withMessage("Vui long nhap ho ten"),
    body("email").isEmail().withMessage("Email khong hop le"),
    body("password")
      .isLength({ min: 6 })
      .withMessage("Mat khau phai co it nhat 6 ky tu")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() });
      }

      const { fullName, email, password } = req.body;
      const existingUser = await User.findOne({ email });
      if (existingUser) {
        return res.status(409).json({ message: "Email da duoc su dung" });
      }

      const hashedPassword = await bcrypt.hash(password, 10);
      const user = await User.create({
        fullName,
        email,
        password: hashedPassword
      });

      const token = signToken(user._id.toString());
      return res.status(201).json({
        token,
        user: {
          id: user._id,
          fullName: user.fullName,
          email: user.email
        }
      });
    } catch (error) {
      next(error);
    }
  }
);

router.post(
  "/login",
  [
    body("email").isEmail().withMessage("Email khong hop le"),
    body("password").notEmpty().withMessage("Vui long nhap mat khau")
  ],
  async (req, res, next) => {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() });
      }

      const { email, password } = req.body;
      const user = await User.findOne({ email });
      if (!user) {
        return res.status(401).json({ message: "Thong tin dang nhap khong dung" });
      }

      const isMatch = await bcrypt.compare(password, user.password);
      if (!isMatch) {
        return res.status(401).json({ message: "Thong tin dang nhap khong dung" });
      }

      const token = signToken(user._id.toString());
      return res.json({
        token,
        user: {
          id: user._id,
          fullName: user.fullName,
          email: user.email
        }
      });
    } catch (error) {
      next(error);
    }
  }
);

module.exports = router;
