export class CreateUserRequest {
  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public isAdmin: boolean
  ) {}
}
