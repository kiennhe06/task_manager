/**
 * Helpers chuan hoa response API.
 * Moi response deu tra ve dang { success, message, data }.
 */

function sendSuccess(res, data = null, message = "Thanh cong", statusCode = 200) {
  return res.status(statusCode).json({
    success: true,
    message,
    data,
  });
}

function sendError(res, message = "Loi he thong", statusCode = 500, errors = null) {
  const body = { success: false, message };
  if (errors) body.errors = errors;
  return res.status(statusCode).json(body);
}

module.exports = { sendSuccess, sendError };
