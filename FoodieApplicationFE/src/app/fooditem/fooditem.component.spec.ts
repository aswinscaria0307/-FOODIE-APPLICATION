import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FooditemComponent } from './fooditem.component';

describe('FooditemComponent', () => {
  let component: FooditemComponent;
  let fixture: ComponentFixture<FooditemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FooditemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FooditemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
