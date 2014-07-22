package api.responses;

import api.models.IModel;

public interface IModelGetResponse<Model extends IModel> extends IGenericResponse {
  public void success(Model model);
}