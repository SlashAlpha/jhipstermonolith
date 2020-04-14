import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISubTask } from 'app/shared/model/sub-task.model';

@Component({
  selector: 'jhi-sub-task-detail',
  templateUrl: './sub-task-detail.component.html'
})
export class SubTaskDetailComponent implements OnInit {
  subTask: ISubTask | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subTask }) => (this.subTask = subTask));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
