import { Router } from "express";
import { UsernameRoute } from "../routes.interface";
import { getAccountsRouter } from "./accounts";
import { getWordsRouter } from "./words";

const getAPIRouter = async () => {
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
    APIRouter.use("/accounts", await getAccountsRouter());
    APIRouter.use("/words", await getWordsRouter());

    return APIRouter;
};

export { getAPIRouter };