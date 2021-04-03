jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RestaurateurService } from '../service/restaurateur.service';
import { IRestaurateur, Restaurateur } from '../restaurateur.model';

import { RestaurateurUpdateComponent } from './restaurateur-update.component';

describe('Component Tests', () => {
  describe('Restaurateur Management Update Component', () => {
    let comp: RestaurateurUpdateComponent;
    let fixture: ComponentFixture<RestaurateurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let restaurateurService: RestaurateurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RestaurateurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RestaurateurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RestaurateurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      restaurateurService = TestBed.inject(RestaurateurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const restaurateur: IRestaurateur = { id: 456 };

        activatedRoute.data = of({ restaurateur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(restaurateur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurateur = { id: 123 };
        spyOn(restaurateurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurateur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(restaurateurService.update).toHaveBeenCalledWith(restaurateur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurateur = new Restaurateur();
        spyOn(restaurateurService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurateur }));
        saveSubject.complete();

        // THEN
        expect(restaurateurService.create).toHaveBeenCalledWith(restaurateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurateur = { id: 123 };
        spyOn(restaurateurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(restaurateurService.update).toHaveBeenCalledWith(restaurateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
