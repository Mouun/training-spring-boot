import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-product-row-actions',
  templateUrl: './product-row-actions.component.html',
  styleUrls: ['./product-row-actions.component.scss'],
  animations: [
    trigger(
      'inOutAnimation',
      [
        transition(
          ':enter',
          [
            style({ opacity: 0, transform: 'scale(.95)' }),
            animate('100ms ease-out',
              style({ opacity: 1, transform: 'scale(1)' }))
          ]
        ),
        transition(
          ':leave',
          [
            style({ opacity: 1, transform: 'scale(1)' }),
            animate('75ms ease-in',
              style({ opacity: 0, transform: 'scale(.95)' }))
          ]
        )
      ]
    )
  ]
})
export class ProductRowActionsComponent implements OnInit {

  public menuShown = false;

  @Output() private whenEditClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() private whenDeleteClicked: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  public onClickEdit(): void {
    this.whenEditClicked.emit();
  }

  public onClickDelete(): void {
    this.whenDeleteClicked.emit();
  }
}
