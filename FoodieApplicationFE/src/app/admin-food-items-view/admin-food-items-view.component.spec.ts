import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFoodItemsViewComponent } from './admin-food-items-view.component';

describe('AdminFoodItemsViewComponent', () => {
  let component: AdminFoodItemsViewComponent;
  let fixture: ComponentFixture<AdminFoodItemsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminFoodItemsViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFoodItemsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
