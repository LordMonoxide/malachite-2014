package api.repositories;

import api.models.IModel;

public interface IRepository<Model extends IModel, PK> {
  public Model get(PK id);
}