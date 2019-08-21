import * as config from "config";

export const PORT: string = config.get("PORT");
export const MONGO_URI: string = config.get("MONGO_URI");
export const DB_NAME: string = config.get("DB_NAME");
export const MONGO_USER: string = config.get("MONGO_USER");
export const MONGO_PW: string = config.get("MONGO_PW");
export const MONGO_ADDRESS: string = config.get("MONGO_ADDRESS");
export const MONGO_PARAMS: string = config.get("MONGO_PARAMS");
export const DB_COLLECTION_USERS: string = config.get("DB_COLLECTION_USERS");
export const DB_COLLECTION_WORDS: string = config.get("DB_COLLECTION_WORDS");
export const ACCOUNTS_SECRET: string = config.get("ACCOUNTS_SECRET");

