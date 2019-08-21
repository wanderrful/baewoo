import { Router } from "express";

// For all endpoints with a Request body, use this header:
//      Content-Type: application/x-www-form-urlencoded;charset=utf-8

const CoreRouter = Router();
CoreRouter.get("/", (req, res) => {
    res.send("Hello world!");
    res.end();
});

export default CoreRouter;