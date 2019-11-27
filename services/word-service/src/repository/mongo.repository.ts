import { connect } from "mongoose";

import { DB_NAME, MONGO_USER, MONGO_PW, MONGO_ADDRESS, MONGO_PARAMS } from "../../config";

const getURI = (dbName: string) => `mongodb+srv://${MONGO_USER}:${MONGO_PW}@${MONGO_ADDRESS}/${dbName}?${MONGO_PARAMS}`;

let _db: typeof import("mongoose");

export const initDb = async callback => {
    if (_db) {
        console.warn("\x1b[33m** \x1b[0mAttempted to initialize DB after connection was made!");
        return callback(null, _db);
    }
    console.log(`\x1b[33m**\t\x1b[0mConnecting to Mongo...`);
    _db = await connect(getURI(DB_NAME), { useNewUrlParser: true, useUnifiedTopology: true, useFindAndModify: false, useCreateIndex: true });
    console.log(`\x1b[32m**\t\x1b[0mConnected to Mongo! \x1b[35m${_db.connections.length}\x1b[0m connection(s) currently live`);
    return callback(null, _db);
};

export const getDb = () => {
    if (!_db) {
        throw new Error("\x1b[31m** \x1b[0mAttempted to get DB but the connection wasn't there!");
    }
    return _db;
};