package api.repositories;

import api.Future;
import api.models.IModel;
import api.responses.IModelGetResponse;

public interface IRepository<Model extends IModel, PK> {
  public Future get(PK id, IModelGetResponse<Model> callback);
}