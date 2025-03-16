package com.jhainusa.arthik_.BackendFiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Database Entity
@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val category: String,
    val subCategory: String,
    val amount: Double,
    val date: Long,
)

// DAO
@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table")
    suspend fun getAllTransactions():List<Transaction>

    @Query("SELECT SUM(amount) as totalAmount FROM transaction_table WHERE category = :category")
    fun getTotalForChatbot(category: String): Int

    @Query("SELECT SUM(amount) FROM transaction_table")
    fun getTotalmoney(): LiveData<Int>

    @Query("SELECT SUM(amount) as totalAmount FROM transaction_table WHERE category = :category")
    fun getTotalForCategorgy(category: String): LiveData<Int>

    @Query("SELECT subCategory, SUM(amount) as totalAmount FROM transaction_table WHERE category = :category GROUP BY subCategory")
    fun getTotalPerSubcategory(category: String): LiveData<List<SubcategoryTotal>>

    @Query("SELECT * FROM transaction_table WHERE category = :category AND subCategory = :subCategory ORDER BY date DESC")
    fun getTransactionsBySubcategory(category: String, subCategory: String): LiveData<List<Transaction>>
}

data class SubcategoryTotal(val subCategory: String, val totalAmount: Double)

// Database
@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Application): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "loan_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Repository
class LoanRepository(private val transactionDao: TransactionDao) {


    fun getTotalForChatbot(category: String) = transactionDao.getTotalForChatbot(category)
    fun getTotalPerSubcategory(category: String) = transactionDao.getTotalPerSubcategory(category)
    fun getTotal_money() = transactionDao.getTotalmoney()
    fun getTotalForCategogry(category: String) = transactionDao.getTotalForCategorgy(category)
    fun getTransactionsBySubcategory(category: String, subCategory: String) = transactionDao.getTransactionsBySubcategory(category, subCategory)
    suspend fun insertTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(transaction)
            val transactions = transactionDao.getAllTransactions()
        }
    }
}

// ViewModel
class LoanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LoanRepository
    init {
        val database = AppDatabase.getDatabase(application)
        repository = LoanRepository(database.transactionDao())
    }
    fun getTotalforChatbot(category: String) = repository.getTotalForChatbot(category)
    fun getTotalmoney() = repository.getTotal_money()
    fun getTotalForCategogry(category: String) = repository.getTotalForCategogry(category)
    fun getTotalPerSubcategory(category: String) = repository.getTotalPerSubcategory(category)
    fun getTransactions(category: String, subCategory: String) = repository.getTransactionsBySubcategory(category, subCategory)
    fun addTransaction(amount: Double, category: String, subCategory: String) {
        viewModelScope.launch {
            repository.insertTransaction(Transaction(amount = amount, category = category, subCategory = subCategory, date = System.currentTimeMillis()))
        }
    }
}