import { Router } from "express";

// For all endpoints with a Request body, use this header:
//      Content-Type: application/x-www-form-urlencoded;charset=utf-8

const CoreRouter = Router();
CoreRouter.get("/", (req, res) => {
    res.send("<a href='/api/v1/auth/discord'>Login</a>");
});

export default CoreRouter;