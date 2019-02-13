export interface IAverageRate {
    avgRate?: number;
    rateAmount?: number;
}

export class AverageRate implements IAverageRate {
    constructor(public avgRate?: number, public rateAmount?: number) {}
}
