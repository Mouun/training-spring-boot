import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/Product';

@Injectable()
export class ProductService {

  private url = '/Produits';

  constructor(private http: HttpClient) { }

  public getAll = (): Observable<Product[]> => {
    return this.http.get<Product[]>(`${this.url}`);
  }

  public get = (id: number): Observable<Product> => {
    return this.http.get<Product>(`${this.url}/${id}`);
  }

  public create = (product: Product): Observable<Product> => {
    return this.http.post<Product>(`${this.url}`, product);
  }

  public update = (product: Product): Observable<void> => {
    return this.http.put<void>(`${this.url}`, product);
  }

  public delete = (id: number): Observable<any> => {
    return this.http.delete(`${this.url}/${id}`);
  }
}

@Injectable()
export class ProductResolver {
  constructor(private productService: ProductService) {}
  resolve(): Observable<Product[]> {
    return this.productService.getAll();
  }
}
