import { Schema } from "mongoose";
import { getDb } from "../repository/mongo.repository";
import { DB_COLLECTION_WORDS } from "../../config/index";

const wordSchema = new Schema({
    english: { type: [String], required: true },
    korean: { type: [String], required: true },
    level: { type: Number, required: true }
});
const db = getDb();
const Word = db.model("Word", wordSchema, DB_COLLECTION_WORDS);

export default Word;