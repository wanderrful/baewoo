import { createServer } from "http";
import * as express from "express";
import * as cors from "cors";

import { TrafficLogger } from "./middleware";

import CoreRouter from "./routes";
import { getAPIRouter } from "./routes/api";

import { PORT } from "../config";
import bodyParser = require("body-parser");

const main = (async () => {
    const app = express();

    // Apply middleware
    // app.use(cors());
    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(bodyParser.json());
    app.use(TrafficLogger);

    // Apply routes
    app.use("/", CoreRouter);
    app.use("/api", await getAPIRouter());

    createServer(app).listen(PORT || 3000, () => console.log(`** \x1b[32mListening on localhost:${PORT || 3000}\x1b[0m`));
})();