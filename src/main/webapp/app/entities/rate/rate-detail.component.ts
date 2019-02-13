import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRate } from 'app/shared/model/rate.model';

@Component({
    selector: 'jhi-rate-detail',
    templateUrl: './rate-detail.component.html'
})
export class RateDetailComponent implements OnInit {
    rate: IRate;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rate }) => {
            this.rate = rate;
        });
    }

    previousState() {
        window.history.back();
    }
}
