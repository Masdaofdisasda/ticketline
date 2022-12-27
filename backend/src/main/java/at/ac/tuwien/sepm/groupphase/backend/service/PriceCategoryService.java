package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

import java.util.List;

public interface PriceCategoryService {

  /**
   * Add a new price category.
   *
   * @param toAdd the data of the price category to add
   * @return the newly added price category
   */
  PriceCategory createPriceCategory(PriceCategory toAdd);

  /**
   * Get a PriceCategory by ID.
   *
   * @param id The id of the price category to fetch
   * @return the price category with the given id
   * @throws NotFoundException if no price category with the given ID exists
   */
  PriceCategory getById(long id) throws NotFoundException;

  /**
   * Get all stored price categories.
   *
   * @return a list of all price categories
   */
  List<PriceCategory> getAllPriceCategories();
}
