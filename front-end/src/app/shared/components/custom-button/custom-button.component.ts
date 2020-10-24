import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-custom-button',
  templateUrl: './custom-button.component.html',
  styleUrls: ['./custom-button.component.scss']
})
export class CustomButtonComponent implements OnInit {

  @Input() public text: string;
  @Input() public type: string;
  @Input() public disabled: boolean;
  @Input() public size: number;
  @Input() public secondary = false;
  @Input() public fullWidth = false;
  @Output() public whenClicked: EventEmitter<void> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }
}
