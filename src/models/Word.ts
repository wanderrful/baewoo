import { Schema, model } from "mongoose";
import { getMongo } from "../repository/mongo.repository";
import { DB_COLLECTION_WORDS } from "../../config/index";

const wordSchema = new Schema({
    english: { type: [String], required: true },
    korean: { type: [String], required: true },
    level: { type: Number, required: true }
});

const getWordModel = async () => {
    const db = await getMongo();
    return db.model("Word", wordSchema, DB_COLLECTION_WORDS);
};

export { getWordModel };