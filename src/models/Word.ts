import { Schema } from "mongoose";
import { getDb } from "../repository/mongo.repository";
import { DB_COLLECTION_WORDS } from "../../config/index";

const wordSchema = new Schema({
    korean: { type: String, required: true },
    english: { type: [String], required: true },
    level: { type: Number, required: true }
});

const Word = getDb().model("Word", wordSchema, DB_COLLECTION_WORDS);

export default Word;