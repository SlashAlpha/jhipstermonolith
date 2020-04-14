import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISubTask, SubTask } from 'app/shared/model/sub-task.model';
import { SubTaskService } from './sub-task.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task/task.service';

@Component({
  selector: 'jhi-sub-task-update',
  templateUrl: './sub-task-update.component.html'
})
export class SubTaskUpdateComponent implements OnInit {
  isSaving = false;
  tasks: ITask[] = [];
  startDateDp: any;
  deadlineDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    duration: [],
    cost: [],
    budget: [],
    started: [],
    difficulty: [],
    startDate: [],
    deadline: [],
    contact: [],
    document: [],
    documentContentType: [],
    task: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected subTaskService: SubTaskService,
    protected taskService: TaskService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subTask }) => {
      this.updateForm(subTask);

      this.taskService.query().subscribe((res: HttpResponse<ITask[]>) => (this.tasks = res.body || []));
    });
  }

  updateForm(subTask: ISubTask): void {
    this.editForm.patchValue({
      id: subTask.id,
      title: subTask.title,
      description: subTask.description,
      duration: subTask.duration,
      cost: subTask.cost,
      budget: subTask.budget,
      started: subTask.started,
      difficulty: subTask.difficulty,
      startDate: subTask.startDate,
      deadline: subTask.deadline,
      contact: subTask.contact,
      document: subTask.document,
      documentContentType: subTask.documentContentType,
      task: subTask.task
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('meApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subTask = this.createFromForm();
    if (subTask.id !== undefined) {
      this.subscribeToSaveResponse(this.subTaskService.update(subTask));
    } else {
      this.subscribeToSaveResponse(this.subTaskService.create(subTask));
    }
  }

  trackById(index: number, item: ITask): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubTask>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private createFromForm(): ISubTask {
    return {
      ...new SubTask(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      cost: this.editForm.get(['cost'])!.value,
      budget: this.editForm.get(['budget'])!.value,
      started: this.editForm.get(['started'])!.value,
      difficulty: this.editForm.get(['difficulty'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      deadline: this.editForm.get(['deadline'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      documentContentType: this.editForm.get(['documentContentType'])!.value,
      document: this.editForm.get(['document'])!.value,
      task: this.editForm.get(['task'])!.value
    };
  }
}
