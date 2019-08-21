import * as mongoose from "mongoose";
import MongoDBInterface from '@accounts/mongo';

import { ACCOUNTS_SECRET, DB_NAME, DB_COLLECTION_USERS, MONGO_USER, MONGO_PW, MONGO_ADDRESS, MONGO_PARAMS } from "../../config";
import AccountsServer from "@accounts/server";
import AccountsPassword from "@accounts/password";

const URI = `mongodb+srv://${MONGO_USER}:${MONGO_PW}@${MONGO_ADDRESS}/${DB_NAME}?${MONGO_PARAMS}`;

export const getMongo = async () => {
    const mongo = await mongoose.connect(URI, { useNewUrlParser: true, useUnifiedTopology: true, useFindAndModify: false, useCreateIndex: true });
    console.log(`** \x1b[32mConnected to Mongo\x1b[0m: ${mongo.connections.length} connection(s) currently live`);
    return mongo.connection;
};

export const getAccountsServerMiddleware = async () => {
    const mongo = await getMongo();
    return new AccountsServer(
        {
            db: new MongoDBInterface(mongo, {
                collectionName: DB_COLLECTION_USERS
            }),
            tokenSecret: ACCOUNTS_SECRET
        },
        {
            password: new AccountsPassword(),
        }
    );
};