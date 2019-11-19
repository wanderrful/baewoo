import * as config from "config";

export const HOSTNAME: string = config.get("HOSTNAME");
export const PORT: string = config.get("PORT");
export const DB_NAME: string = config.get("DB_NAME");
export const MONGO_USER: string = config.get("MONGO_USER");
export const MONGO_PW: string = config.get("MONGO_PW");
export const MONGO_ADDRESS: string = config.get("MONGO_ADDRESS");
export const MONGO_PARAMS: string = config.get("MONGO_PARAMS");
export const DB_COLLECTION_USERS: string = config.get("DB_COLLECTION_USERS");
export const DB_COLLECTION_WORDS: string = config.get("DB_COLLECTION_WORDS");
export const SESSION_SECRET: string = config.get("SESSION_SECRET");
export const DISCORD_CLIENT_ID: string = config.get("DISCORD_CLIENT_ID");
export const DISCORD_CLIENT_SECRET: string = config.get("DISCORD_CLIENT_SECRET");

