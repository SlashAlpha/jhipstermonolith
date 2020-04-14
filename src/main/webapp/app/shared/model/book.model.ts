export interface IBook {
  id?: number;
  titleetitle?: number;
}

export class Book implements IBook {
  constructor(public id?: number, public titleetitle?: number) {}
}
