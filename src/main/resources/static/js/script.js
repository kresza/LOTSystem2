 function searchFlights() {
            var id = encodeURIComponent(document.getElementById('id').value);
            var flightNumber = encodeURIComponent(document.getElementById('flightNumber').value);
            var startingPlace = encodeURIComponent(document.getElementById('startingPlace').value);
            var destination = encodeURIComponent(document.getElementById('destination').value);
            var flightDate = encodeURIComponent(document.getElementById('flightDate').value);
            var seats = encodeURIComponent(document.getElementById('seats').value);

            var url = '/search_flight?'
                + 'id=' + id
                + '&flightNumber=' + flightNumber
                + '&startingPlace=' + startingPlace
                + '&destination=' + destination
                + '&flightDate=' + flightDate
                + '&seats=' + seats;

            window.location.href = url;
 }

   function sortFlights(selectElement) {
         var column = selectElement.dataset.column;
         var sortOrder = selectElement.value;

         sessionStorage.setItem('sortColumn', column);
         sessionStorage.setItem('sortOrder', sortOrder);
         var ascUrl = '/flights/ASC/' + column;
         var descUrl = '/flights/DESC/' + column;
         var url = sortOrder === 'asc' ? ascUrl : descUrl;

         fetch(url)
             .then(response => response.text())
             .then(html => {
                 var doc = new DOMParser().parseFromString(html, 'text/html');
                 var newTable = doc.querySelector('#flight-table-body').innerHTML;
                 var tableBody = document.getElementById('flight-table-body');
                 tableBody.innerHTML = newTable;
             })
             .catch(error => {
                 console.error('Error:', error);
             });
     }
     window.onload = function() {
         var sortColumn = sessionStorage.getItem('sortColumn');
         var sortOrder = sessionStorage.getItem('sortOrder');
         if (sortColumn && sortOrder) {
             var selectElement = document.querySelector(`select[data-column="${sortColumn}"]`);
             if (selectElement) {
                 selectElement.value = sortOrder;
             }
         } else {
             resetAllSelectsToLine();
         }
     };