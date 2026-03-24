require("dotenv").config();
var createError = require("http-errors");
var express = require("express");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
var cors = require("cors");

var connectDb = require("./config/db");
var authRouter = require("./routes/auth");
var taskRouter = require("./routes/tasks");

connectDb();

var app = express();

app.use(cors());
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.get("/api/health", function (req, res) {
  res.json({ status: "ok", service: "task-manager-backend" });
});
app.use("/api/auth", authRouter);
app.use("/api/tasks", taskRouter);

app.use(function (req, res, next) {
  next(createError(404, "Khong tim thay duong dan"));
});

app.use(function (err, req, res, next) {
  var status = err.status || 500;
  res.status(status).json({
    message: err.message || "Loi he thong",
    status: status
  });
});

module.exports = app;
