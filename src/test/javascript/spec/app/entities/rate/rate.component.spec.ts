/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ArticleSharingAppTestModule } from '../../../test.module';
import { RateComponent } from 'app/entities/rate/rate.component';
import { RateService } from 'app/entities/rate/rate.service';
import { Rate } from 'app/shared/model/rate.model';

describe('Component Tests', () => {
    describe('Rate Management Component', () => {
        let comp: RateComponent;
        let fixture: ComponentFixture<RateComponent>;
        let service: RateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticleSharingAppTestModule],
                declarations: [RateComponent],
                providers: []
            })
                .overrideTemplate(RateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RateService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Rate(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
