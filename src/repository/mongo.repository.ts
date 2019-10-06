import { connect } from "mongoose";
import MongoDBInterface from '@accounts/mongo';

import { ACCOUNTS_SECRET, DB_NAME, DB_COLLECTION_USERS, MONGO_USER, MONGO_PW, MONGO_ADDRESS, MONGO_PARAMS } from "../../config";
import AccountsServer from "@accounts/server";
import AccountsPassword from "@accounts/password";

const getURI = (dbName: string) => `mongodb+srv://${MONGO_USER}:${MONGO_PW}@${MONGO_ADDRESS}/${dbName}?${MONGO_PARAMS}`;

let _db: typeof import("mongoose");

export const initDb = async callback => {
    if (_db) {
        console.warn("\x1b[33m** \x1b[0mAttempted to initialize DB after connection was made!");
        return callback(null, _db);
    }
    _db = await connect(getURI(DB_NAME), { useNewUrlParser: true, useUnifiedTopology: true, useFindAndModify: false, useCreateIndex: true });
    console.log(`** \x1b[32mConnected to Mongo\x1b[0m: ${_db.connections.length} connection(s) currently live`);
    return callback(null, _db);
};

export const getDb = () => {
    if (!_db) {
        throw new Error("\x1b[31m** \x1b[0mAttempted to get DB but the connection wasn't there!");
    }
    return _db;
};

export const getAccountsServerMiddleware = () => {
    const mongo = getDb();
    return new AccountsServer(
        {
            db: new MongoDBInterface(mongo.connection.db, {
                collectionName: DB_COLLECTION_USERS
            }),
            tokenSecret: ACCOUNTS_SECRET
        },
        {
            password: new AccountsPassword(),
        }
    );
};