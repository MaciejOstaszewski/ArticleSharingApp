/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ArticleSharingAppTestModule } from '../../../test.module';
import { RateUpdateComponent } from 'app/entities/rate/rate-update.component';
import { RateService } from 'app/entities/rate/rate.service';
import { Rate } from 'app/shared/model/rate.model';

describe('Component Tests', () => {
    describe('Rate Management Update Component', () => {
        let comp: RateUpdateComponent;
        let fixture: ComponentFixture<RateUpdateComponent>;
        let service: RateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticleSharingAppTestModule],
                declarations: [RateUpdateComponent]
            })
                .overrideTemplate(RateUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RateUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RateService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Rate(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rate = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Rate();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rate = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
