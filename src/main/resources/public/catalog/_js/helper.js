export function generateRange(count) {
  return Array.from(Array(count).keys());
}
export function changeSortOption(sorting, newSortField) {
  if (sorting.field === newSortField) {
    sorting.asc = !sorting.asc;
    return sorting;
  } else {
    sorting.field = newSortField;
    return sorting;
  }
}
// установка флажка по значениям checkedArray
export function setCheckBoxes(checkboxes, checkedArray) {
  checkboxes.forEach((checkbox) => {
    if (checkedArray.includes(parseInt(checkbox.id))) {
      checkbox.checked = true;
    }
  });
}
export function getSelectedValues(checkboxes) {
  const selectedValues = [];

  checkboxes.forEach((checkbox) => {
    if (checkbox.checked) {
      selectedValues.push(+checkbox.id);
    }
  });

  return selectedValues;
}
