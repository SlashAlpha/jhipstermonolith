import { ITask } from 'app/shared/model/task.model';
import { IUser } from 'app/core/user/user.model';

export interface IProject {
  id?: number;
  name?: string;
  tasks?: ITask[];
  user?: IUser;
}

export class Project implements IProject {
  constructor(public id?: number, public name?: string, public tasks?: ITask[], public user?: IUser) {}
}
