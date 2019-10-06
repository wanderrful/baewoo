/* This file defines routes for the prefix `/api/v1/` */
import { Router } from "express";
import { UsernameRoute } from "../routes.interface";
import AccountsRouter from "./accounts";
import WordsRouter from "./words";

const APIRouter = Router();

APIRouter.use((req, res, next) => {
    console.log(`** API ${req.method} uri=${req.path}`);
    next();
});

APIRouter.get("/", (req, res) => {
    res.send("Hello API!");
    res.end();
});

// Apply sub-routes
APIRouter.use("/accounts", AccountsRouter);
APIRouter.use("/words", WordsRouter);

export default APIRouter;