/* This file defines routes for the prefix `/api/v1/accounts/` */
import { Router } from "express";

import accountsExpress, { userLoader } from '@accounts/rest-express';

import { getAccountsServerMiddleware } from "../../../repository/mongo.repository";
import { CreateUserRoute } from "routes/routes.interface";

const accountsServer = getAccountsServerMiddleware();

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

AccountsRouter.post("/user", (req: CreateUserRoute, res) => {
    const { username, password } = req.body;
    res.send(`TO DO: I want to make an account for ${username} with a password of ${password}`);
    res.end();
});

export default AccountsRouter;