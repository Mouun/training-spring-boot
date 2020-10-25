import { Component, OnInit, ViewChild } from '@angular/core';
import { SwalComponent } from '@sweetalert2/ngx-sweetalert2';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  @ViewChild('addOrEditSwal') private addOrEditSwal: SwalComponent;
  @ViewChild('deleteSwal') private deleteSwal: SwalComponent;

  public popupTitle: string;
  public createOrUpdateProductForm: FormGroup;
  public addMode = false;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.createOrUpdateProductForm = this.formBuilder.group({
      id: new FormControl(''),
      nom: new FormControl('', [Validators.required]),
      prix: new FormControl('', [Validators.required]),
      prixAchat: new FormControl('', [Validators.required]),
    });
  }

  public onAddClicked(): void {
    this.createOrUpdateProductForm.reset();
    this.addMode = true;
    this.popupTitle = 'Ajouter un produit';
    this.addOrEditSwal.fire();
  }

  public onEditClicked(): void {
    this.popupTitle = 'Editer un produit';
    this.createOrUpdateProductForm.patchValue({
      id: 0,
      nom: 'iPhone 12',
      prix: 1000,
      prixAchat: 800
    });
    this.addOrEditSwal.fire();
  }

  public deleteProduct(): void {
    console.log('delete');
  }

  public addProduct(): void {
  }

  public editProduct(): void {
  }
}
