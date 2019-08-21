interface Response {
    data: object;
    message: string;
}

export class BaewooResponse {
    response: Response;

    constructor(response: Response) {
        this.response = response;
    }

    render() {
        return this.response;
    }
}