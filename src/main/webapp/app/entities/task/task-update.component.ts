import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ITask, Task } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;
  projects: IProject[] = [];
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
    project: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected taskService: TaskService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));
    });
  }

  updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      title: task.title,
      description: task.description,
      duration: task.duration,
      cost: task.cost,
      budget: task.budget,
      started: task.started,
      difficulty: task.difficulty,
      startDate: task.startDate,
      deadline: task.deadline,
      contact: task.contact,
      document: task.document,
      documentContentType: task.documentContentType,
      project: task.project
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
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  trackById(index: number, item: IProject): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
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

  private createFromForm(): ITask {
    return {
      ...new Task(),
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
      project: this.editForm.get(['project'])!.value
    };
  }
}
