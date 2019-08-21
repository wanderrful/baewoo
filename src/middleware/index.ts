const Router = require("express").Router();

export const TrafficLogger = Router.use("/", (req, res, next) => {
    console.log(`* Incoming: ${req.method} ${req.path}`);
    if (req.method === "PUT") {
        console.log(`-- Body: ${JSON.stringify(req.body)}`);
    }
    next();
})