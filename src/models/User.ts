import { Schema } from "mongoose";
import { getDb } from "../repository/mongo.repository";
import { DB_COLLECTION_USERS } from "../../config/index";

const userSchema = new Schema({
    username: { type: String, required: true },
    discriminator: { type: String, required: true },
    provider: { type: String, required: true },
    avatar: { type: String, required: false },
    locale: { type: String, required: false },
    accessToken: { type: String, required: false },
    refreshToken: { type: String, required: false }
});

const User = getDb().model("User", userSchema, DB_COLLECTION_USERS);

export default User;