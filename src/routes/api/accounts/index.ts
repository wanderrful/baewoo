import { Router } from "express";

import accountsExpress, { userLoader } from '@accounts/rest-express';

import { getAccountsServerMiddleware } from "../../../repository/mongo.repository";
import { UsernameRoute } from "routes/routes.interface";
import * as Word from "models/Word";

const getAccountsRouter = async () => {
    const accountsServer = await getAccountsServerMiddleware();

    const AccountsRouter = Router();

    // Apply AccountsJS middleware
    AccountsRouter.use(accountsExpress(accountsServer));

    AccountsRouter.get("/", (req, res) => {
        res.send("You have reached the Accounts API route root. Please leave a message after the beep. *beep*");
        res.end();
    })

    AccountsRouter.get("/user", userLoader(accountsServer), (req, res) => {
        res.json({ user: (req as any).user });
        res.end();
    });

    AccountsRouter.get("/user/:username/:password", (req: UsernameRoute, res) => {
        const { username, password } = req.params;
        res.send(`TO DO: I want to make an account for ${username} with a password of ${password}`);
        res.end();
    });

    return AccountsRouter;
}

export {
    getAccountsRouter
};