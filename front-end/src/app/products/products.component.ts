import { Component, OnInit, ViewChild } from '@angular/core';
import { SwalComponent } from '@sweetalert2/ngx-sweetalert2';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../shared/models/Product';
import { ProductService } from '../shared/services/product.service';

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
  public productToEditId: number;
  public productToDeleteId: number;

  public products: Product[];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private productService: ProductService
  ) {
  }

  ngOnInit(): void {
    this.route.data
      .subscribe(data => {
        this.products = data.products;

        this.createOrUpdateProductForm = this.formBuilder.group({
          id: new FormControl(''),
          nom: new FormControl('', [Validators.required]),
          prix: new FormControl('', [Validators.required]),
          prixAchat: new FormControl('', [Validators.required]),
        });
      });
  }

  public onAddClicked(): void {
    this.createOrUpdateProductForm.reset();
    this.addMode = true;
    this.popupTitle = 'Ajouter un produit';
    this.addOrEditSwal.fire();
  }

  public onEditClicked(productId: number): void {
    this.productToEditId = productId;
    this.popupTitle = 'Editer un produit';

    const productToEdit = this.products.find(x => x.id === productId);

    this.createOrUpdateProductForm.patchValue({
      id: productToEdit.id,
      nom: productToEdit.nom,
      prix: productToEdit.prix,
      prixAchat: productToEdit.prixAchat
    });

    this.addOrEditSwal.fire();
  }

  public onDeleteCliked(productId: number): void {
    this.productToDeleteId = productId;
    this.deleteSwal.fire();
  }

  public deleteProduct(): void {
    this.productService.delete(this.productToDeleteId).subscribe(() => {
      const productToDeleteIndex = this.products.findIndex(x => x.id === this.productToDeleteId);
      this.products.splice(productToDeleteIndex, 1);
      this.deleteSwal.dismiss();
    });
  }

  public addProduct(): void {
    const productToAdd = this.createOrUpdateProductForm.getRawValue() as Product;
    this.productService.create(productToAdd).subscribe(product => {
      this.products.push(product);
      this.closeAddOrEditSwal();
    });
  }

  public editProduct(): void {
    const productToUpdate = this.createOrUpdateProductForm.getRawValue() as Product;
    this.productService.update(productToUpdate).subscribe(() => {
      const productToUpdateIndex = this.products.findIndex(x => x.id === productToUpdate.id);
      this.products[productToUpdateIndex] = productToUpdate;
      this.closeAddOrEditSwal();
    });
  }

  public closeAddOrEditSwal(): void {
    this.addMode = false;
    this.createOrUpdateProductForm.reset();
    this.addOrEditSwal.dismiss();
  }
}
