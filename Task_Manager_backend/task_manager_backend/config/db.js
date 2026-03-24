const mongoose = require("mongoose");

async function connectDb() {
  const mongoUri = process.env.MONGO_URI;
  if (!mongoUri) {
    console.error("Thieu MONGO_URI. Vui long them vao bien moi truong.");
    return;
  }

  try {
    await mongoose.connect(mongoUri);
    console.log("Da ket noi MongoDB");
  } catch (error) {
    console.error("Loi ket noi MongoDB:", error.message);
    process.exit(1);
  }
}

module.exports = connectDb;
