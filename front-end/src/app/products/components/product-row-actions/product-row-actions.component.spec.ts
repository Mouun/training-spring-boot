import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductRowActionsComponent } from './product-row-actions.component';

describe('ProductRowActionsComponent', () => {
  let component: ProductRowActionsComponent;
  let fixture: ComponentFixture<ProductRowActionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductRowActionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductRowActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
