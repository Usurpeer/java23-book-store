function sorted(arr, sorting) {
  const arrCopy = [...arr];
  arrCopy.sort(
    (i1, i2) =>
      ((i1[sorting.field] > i2[sorting.field]) -
        (i1[sorting.field] < i2[sorting.field])) *
      (sorting.asc || -1)
  );
  return arrCopy;
}

//export
function changeSorting(sorting, field) {
  if (field == sorting.field) {
    sorting.asc = !sorting.asc;
  } else {
    sorting.field = field;
    sorting.asc = true;
  }
  return sorting;
}
