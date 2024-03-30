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
export function setCheckBoxes(checkboxes, checkedArray, isPublishers) {
  if (isPublishers) {
    checkboxes.forEach((checkbox) => {
      let label = document.querySelector(`label[for="${checkbox.id}"]`);
      if (checkedArray.includes(label.textContent)) {
        checkbox.checked = true;
      }
    });
  } else {
    checkboxes.forEach((checkbox) => {
      let id = +checkbox.id.substring(0, checkbox.id.length - 1);
      if (checkedArray.includes(parseInt(id))) {
        checkbox.checked = true;
      }
    });
  }
}
export function getSelectedValues(checkboxes, isPublishers) {
  const selectedValues = [];

  if (isPublishers) {
    checkboxes.forEach((checkbox) => {
      if (checkbox.checked) {
        let label = document.querySelector(`label[for="${checkbox.id}"]`);
        selectedValues.push(label.textContent);
      }
    });
  } else {
    checkboxes.forEach((checkbox) => {
      if (checkbox.checked) {
        let id = +checkbox.id.substring(0, checkbox.id.length - 1);
        selectedValues.push(id);
      }
    });
  }

  return selectedValues;
}
export function mapSearchValue(str) {
  str = str.toLowerCase();
  str = str.trim();
  str = str.replace(/\s+/g, " ");
  str = str.replace(/[^\w\s]|_/g, "");
  return str;
}
