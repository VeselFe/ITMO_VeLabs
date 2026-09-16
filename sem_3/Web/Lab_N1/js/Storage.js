function formatDate( dateString ) 
{
     const date = new Date( dateString );
     return new Intl.DateTimeFormat('ru-RU', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
     }).format(date);
}

function append( data )
{
    const table = document.getElementById( 'res-body' );
    if( !table ) return;

    const row = document.createElement( 'tr' );

    row.innerHTML = `<td>${data.x}</td>
                     <td>${data.y}</td>
                     <td>${data.r}</td>
                     <td>
                        ${data.success ? 'точка принадлежит' : 'точка не принадлежит'}
                     </td>
                     <td>
                        ${formatDate( data.time )}
                     </td>`;
    table.insertBefore( row, table.firstChild );
}

const STORAGE_NAME = "storage";
function getHistory()
{
    const rawData = localStorage.getItem( STORAGE_NAME );
    if( rawData )
        return JSON.parse( rawData );
    return [];
}

function save( x, y, r, success )
{
    const history = getHistory();
    const newRes = {
        x: x,
        y: y,
        r: r,
        success: success,
        time: new Date().toISOString()
    };
    history.push( newRes );
    localStorage.setItem( STORAGE_NAME, JSON.stringify(history) );
    append( newRes );
}

function load()
{
    const history = getHistory();
    const table = document.getElementById( 'res-body' );
    if( table ) table.innerHTML = '';

    history.forEach( res => append(res) );
}

document.addEventListener( 'DOMContentLoaded', load );
window.addEventListener( 'focus', load );

document.addEventListener( 'visibilitychange', () => {
    if( document.visibilityState === 'visible' ) 
    {
        load();
    }
});