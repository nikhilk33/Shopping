package com.example.myshoppingapp

import android.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing:Boolean=false
)

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myshoppingapp(){
    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    
 Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(top = 24.dp), // Add top padding to move content below status bar
    verticalArrangement = Arrangement.Top // Change from Center to Top
) {
    // Add some space before the button
    Modifier.padding(top = 16.dp)

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
           .align(Alignment.CenterHorizontally)
            .padding(top = 16.dp) // Additional padding for the button itself
    ) {
        Text("Add Items")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
    items(sItems) {
        //ShoppingListItem(it,{},{})
            item ->
        if (item.isEditing) {
            ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                        sItems = sItems.map { currentItem ->
                            if (currentItem.id == item.id) {
                                currentItem.copy(
                                    name = editedName,
                                    quantity = editedQuantity,
                                    isEditing = false
                                )
                            } else {
                                currentItem
                            }
                        }
                    })
                }else{
                    ShoppingListItem(item= item, onEditClick = {
                        //finding out which items we are editing and changing is "isEditing boolean " to true
                        sItems=sItems.map { it.copy(isEditing=it.id==item.id) } },
                        onDeleteClick = {
                            sItems=sItems.filterNot { it.id==item.id }

                    })

                }
            }
        }
    }
    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog=false},
            confirmButton={
                Row(modifier=Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick={
                        if(itemName.isNotBlank() && itemQuantity.isNotBlank()){
                            val newItem=ShoppingItem(id=sItems.size+1,
                                name=itemName,
                                quantity=itemQuantity.toIntOrNull() ?:1 )
                            sItems=sItems+newItem
                            showDialog=false
                            itemName=""
                            itemQuantity=""
                        }

                    }){
                        Text("Add")
                    }
                    Button(onClick={showDialog=false }){
                        Text("Cancel")
                    }
                }
            },
            title = {Text("Add Shopping items!!")},
            text = {
                Column {
                    OutlinedTextField(value= itemName,
                        onValueChange ={itemName=it},
                        singleLine = true,
                        modifier=Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField(value= itemQuantity,
                        onValueChange ={itemQuantity=it},
                        singleLine = true,
                        modifier=Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            })
        }


}
@Composable
fun ShoppingItemEditor(item:ShoppingItem,onEditComplete:(String,Int)->Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
   // var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(modifier=Modifier.fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(
            value =editedName,
                onValueChange ={
                    editedName=it
                },
                singleLine = true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value =editedQuantity,
                onValueChange ={
                    editedQuantity=it
                },
                singleLine = true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )



        }
        Button(
            onClick = {
               // isEditing=false
                onEditComplete(editedName,editedQuantity.toIntOrNull() ?: 1)
            }
        ){
            Text("Save")
        }
    }

}
@Composable
fun ShoppingListItem(
    item:ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit,

){
    Row(
        modifier=Modifier.padding(8.dp).fillMaxWidth().border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text=item.name,modifier=Modifier.padding(8.dp))
        Text(text="Qty: ${item.quantity}",modifier=Modifier.padding(8.dp))
        Row(modifier=Modifier.padding(8.dp)){
            IconButton(onClick=onEditClick){
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick=onDeleteClick){
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
