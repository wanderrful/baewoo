import { createServer } from "http";
import * as express from "express";
import * as cors from "cors";
import * as bodyParser from "body-parser";

import { TrafficLogger } from "./middleware";

import { PORT } from "../config";

import { initDb } from "./repository/mongo.repository";

initDb(async () => {
    const app = express();

    // Apply middleware
    app.use(cors());
    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(bodyParser.json());
    app.use(TrafficLogger);

    // Load routers (requires DB connection to import)
    const CoreRouter = await import("./routes");
    const APIRouter = await import("./routes/api");

    // Apply routes
    app.use("/", CoreRouter.default);
    app.use("/api/v1", APIRouter.default);

    createServer(app)
        .listen(PORT || 3000, () => console.log(`\x1b[35m** \x1b[0mListening on localhost:\x1b[32m${PORT || 3000}\x1b[0m`));
}).catch(err => console.error(err));