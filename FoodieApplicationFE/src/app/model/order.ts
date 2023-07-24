import { Item } from "./item";

export class Order {
    customerId: string|undefined;
    billingAddress: string|undefined;
    items: Item[]|undefined;
  }